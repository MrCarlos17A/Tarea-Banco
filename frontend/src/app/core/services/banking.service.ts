import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, forkJoin, map, switchMap, tap } from 'rxjs';
import {
  Cliente,
  CrearClientePayload,
  ProductoNombre,
  Solicitud
} from '../models/banking.models';

@Injectable({ providedIn: 'root' })
export class BankingService {
  private readonly http = inject(HttpClient);

  readonly clientes = signal<Cliente[]>([]);
  readonly solicitudes = signal<Solicitud[]>([]);
  readonly loading = signal<boolean>(false);
  readonly selectedClienteId = signal<number | null>(null);

  readonly clientesMap = computed(() => {
    const map = new Map<number, Cliente>();
    for (const c of this.clientes()) {
      map.set(c.id, c);
    }
    return map;
  });

  readonly solicitudesPorCliente = computed(() => {
    const counts = new Map<number, number>();
    for (const s of this.solicitudes()) {
      counts.set(s.clienteId, (counts.get(s.clienteId) ?? 0) + 1);
    }
    return counts;
  });

  readonly totalSolicitudes = computed(() => this.solicitudes().length);
  readonly totalClientes = computed(() => this.clientes().length);
  readonly totalTarjetas = computed(
    () => this.solicitudes().filter((s) => s.producto === 'Tarjeta de crédito').length
  );
  readonly totalPrestamos = computed(
    () => this.solicitudes().filter((s) => s.producto === 'Préstamo personal').length
  );
  readonly totalCuentas = computed(
    () => this.solicitudes().filter((s) => s.producto === 'Cuenta de ahorros').length
  );

  refreshAll(preferClientId?: number): Observable<{ clientes: Cliente[]; solicitudes: Solicitud[] }> {
    this.loading.set(true);
    return forkJoin({
      clientes: this.http.get<Cliente[]>('/api/clientes'),
      solicitudes: this.http.get<Solicitud[]>('/api/solicitudes')
    }).pipe(
      tap({
        next: ({ clientes, solicitudes }) => {
          this.clientes.set(clientes);
          this.solicitudes.set(solicitudes);
          if (preferClientId && clientes.some((c) => c.id === preferClientId)) {
            this.selectedClienteId.set(preferClientId);
          } else if (
            this.selectedClienteId() !== null &&
            clientes.some((c) => c.id === this.selectedClienteId())
          ) {
            // keep current selection
          } else {
            this.selectedClienteId.set(clientes.length > 0 ? clientes[0].id : null);
          }
          this.loading.set(false);
        },
        error: () => {
          this.loading.set(false);
        }
      })
    );
  }

  registrarCliente(payload: CrearClientePayload): Observable<{ id: number }> {
    return this.http.post<{ id: number }>('/api/clientes', payload).pipe(
      switchMap((res) => this.refreshAll(res.id).pipe(map(() => res)))
    );
  }

  crearSolicitud(clienteId: number, producto: ProductoNombre): Observable<{ id: number }> {
    return this.http.post<{ id: number }>('/api/solicitudes', { clienteId, producto }).pipe(
      switchMap((res) => this.refreshAll(clienteId).pipe(map(() => res)))
    );
  }

  clear(): void {
    this.clientes.set([]);
    this.solicitudes.set([]);
    this.selectedClienteId.set(null);
    this.loading.set(false);
  }
}
