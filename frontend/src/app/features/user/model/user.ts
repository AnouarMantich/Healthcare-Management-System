export interface User {
  cin: string | null;
  username: string;
  email: string;
  profileCompleted: boolean;
  firstName: string;
  lastName: string;
  fullName: string;
  address: string | null;
  phoneNumber: string | null;
  role: string;
  createdAt: string;
}
