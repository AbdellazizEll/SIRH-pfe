import {Roles} from "./roles";

export interface Collaborator {
  id: number;
  firstname: string;
  lastname: string;
  age: number;
  email: string;
  password: string;

  roles?: Array<Roles>;

  isCollab: boolean;
  isEnabled: boolean;
  isGestRH:boolean;
  isManager:boolean;
  phone: string;
  createdAt: Date;




}

