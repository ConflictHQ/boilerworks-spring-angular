export interface Product {
  id: string;
  name: string;
  slug: string;
  description: string;
  price: number;
  sku: string;
  active: boolean;
  categoryId: string;
  categoryName: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateProductRequest {
  name: string;
  description?: string;
  price: number;
  sku: string;
  active?: boolean;
  categoryId?: string;
}

export interface UpdateProductRequest {
  name?: string;
  description?: string;
  price?: number;
  sku?: string;
  active?: boolean;
  categoryId?: string;
}
