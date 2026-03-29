export interface Item {
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

export interface CreateItemRequest {
  name: string;
  description?: string;
  price: number;
  sku: string;
  active?: boolean;
  categoryId?: string;
}

export interface UpdateItemRequest {
  name?: string;
  description?: string;
  price?: number;
  sku?: string;
  active?: boolean;
  categoryId?: string;
}
