export interface AuthUser {
  email : string;
  expired: boolean;
  locked: boolean;
  credentailsExpired: boolean;
  enabled : boolean;
  roles: string[];
}
