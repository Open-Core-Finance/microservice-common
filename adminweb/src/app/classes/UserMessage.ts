export class UserMessage {
    public success: any[];
    public error: any[];
  
    constructor(success: any[], error: any[]) {
      this.success = success;
      this.error = error;
    }
  
    clearSuccess() {
      this.success = [];
    }

    clearError() {
      this.error = [];
    }

    clearAll() {
      this.clearSuccess();
      this.clearError();
    }
}

export class MessageItem {
  key: string = "";
  data: string[] = [];
}