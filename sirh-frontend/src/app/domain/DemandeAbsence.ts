import {Collaborator} from "./collaborator";

export interface DemandeAbsence {
  dateDebut: Date;
  dateFin: Date;
  comment: string;
  motif: string; // Assuming Status is an enum or string in backend
}
