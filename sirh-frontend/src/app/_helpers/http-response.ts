export interface HttpResponse<T> {
  timestamp: string;
  statusCode: number;
  status: string;
  message: string;
  data: T;
}
