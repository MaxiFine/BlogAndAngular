import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface BlogPost {
  id?: string;
  name: string;
  content: string;
  postedBy: string;
  image: string;
  createdAt?: string;
  blogTags?: string[];
  likeCounts?: number;
  viewCount?: number;
}

export interface PageResponse<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
  };
  totalPages: number;
  totalElements: number;
  last: boolean;
  first: boolean;
  size: number;
  number: number;
  numberOfElements: number;
  empty: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class BlogService {
  private apiUrl = 'http://localhost:8080/api/v1/blog';

  constructor(private http: HttpClient) { }

  createBlog(blogData: BlogPost): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/post`, blogData, { responseType: 'text' as 'json' });
  }

  getAllBlogs(page: number = 0, size: number = 10): Observable<PageResponse<BlogPost>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<BlogPost>>(`${this.apiUrl}/all-posts`, { params });
  }

  getBlogDetails(blogId: string): Observable<BlogPost> {
    return this.http.get<BlogPost>(`${this.apiUrl}/get-details/${blogId}`);
  }

  getBlogByName(name: string): Observable<BlogPost> {
    const params = new HttpParams().set('name', name);
    return this.http.get<BlogPost>(`${this.apiUrl}/find-name`, { params });
  }

  updateBlog(blogId: string, blogData: BlogPost): Observable<BlogPost> {
    return this.http.put<BlogPost>(`${this.apiUrl}/update-blog/${blogId}`, blogData);
  }

  likePost(postId: string): Observable<string> {
    return this.http.patch<string>(`${this.apiUrl}/like/${postId}`, {}, { responseType: 'text' as 'json' });
  }
}
