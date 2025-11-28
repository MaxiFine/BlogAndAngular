import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Comment {
  id?: string;
  commentText: string;
  createdAt?: string;
  postedBy: string;
}

export interface CommentResponse {
  id: string;
  commentText: string;
  createdAt: string;
  postedBy: string;
}

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiUrl = 'http://localhost:8080/api/v1/comments';

  constructor(private http: HttpClient) { }

  createComment(postId: string, comment: Comment): Observable<CommentResponse> {
    return this.http.post<CommentResponse>(`${this.apiUrl}/${postId}`, comment);
  }

  getCommentsByPostId(postId: string): Observable<CommentResponse[]> {
    return this.http.get<CommentResponse[]>(`${this.apiUrl}/get-comments/${postId}`);
  }
}
