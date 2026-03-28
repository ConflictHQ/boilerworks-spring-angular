export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  active: boolean;
  staff: boolean;
  groups: string[];
  permissions: string[];
}
