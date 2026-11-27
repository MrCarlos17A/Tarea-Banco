export type ProductoNombre =
  | 'Tarjeta de crédito'
  | 'Préstamo personal'
  | 'Cuenta de ahorros';

export interface Cliente {
  id: number;
  nombre: string;
  doc: string;
  email?: string;
}

export interface CrearClientePayload {
  nombre: string;
  doc: string;
  email?: string;
}

export interface Solicitud {
  id: number;
  clienteId: number;
  producto: ProductoNombre | string;
  estado: string;
  creada: string;
}

export interface ProductOption {
  nombre: ProductoNombre;
  resumen: string;
  icon: 'credit-card' | 'banknote' | 'piggy-bank';
}

export const PRODUCT_OPTIONS: ProductOption[] = [
  {
    nombre: 'Tarjeta de crédito',
    resumen: 'Compras y beneficios',
    icon: 'credit-card'
  },
  {
    nombre: 'Préstamo personal',
    resumen: 'Cuotas fijas mensuales',
    icon: 'banknote'
  },
  {
    nombre: 'Cuenta de ahorros',
    resumen: 'Sin costo de mantenimiento',
    icon: 'piggy-bank'
  }
];
