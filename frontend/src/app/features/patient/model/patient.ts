import { User } from '../../user/model/user';

export interface Patient {
  id: string;
  user: User;
  bloodType: string;
  allergies: string | null;
  chronicDiseases: string | null;
  insuranceNumber: string | null;
  emergencyContact: string;
}
