import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MessageRequest } from '../interfaces/api/messageRequest.interface';
import { MessageResponse } from '../interfaces/api/messageResponse.interface';

@Injectable({
  providedIn: 'root'
})
export class MessagesService {

  private pathService = 'http://localhost:3001/api/messages';

  constructor(private httpClient: HttpClient) { }

  public send(messageRequest: MessageRequest): Observable<MessageResponse> {
    console.log('Sending message:', messageRequest);
    return this.httpClient.post<MessageResponse>(this.pathService, messageRequest);
  }
  }
