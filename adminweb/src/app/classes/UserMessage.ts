export class UserMessage {
    public success: any[];
    public error: any[];
  
    constructor(success: any[], error: any[]) {
      this.success = success;
      this.error = error;
    }
  
  }