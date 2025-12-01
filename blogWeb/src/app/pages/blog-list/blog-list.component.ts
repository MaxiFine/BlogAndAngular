import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { BlogService, BlogPost, PageResponse } from '../../services/blog.service';
import { MaterialModules } from '../../AngularMaterialModule';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-blog-list',
  standalone: true,
  imports: [MaterialModules, CommonModule],
  templateUrl: './blog-list.component.html',
  styleUrl: './blog-list.component.css'
})
export class BlogListComponent implements OnInit {
  blogs: BlogPost[] = [];
  currentPage = 0;
  pageSize = 9;
  totalPages = 0;
  totalElements = 0;
  isLoading = false;

  constructor(
    private blogService: BlogService,
    private router: Router,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.loadBlogs();
  }

  loadBlogs(): void {
    this.isLoading = true;
    this.blogService.getAllBlogs(this.currentPage, this.pageSize).subscribe({
      next: (response: any) => {
        this.blogs = response.content;
        this.totalPages = response.totalPages || 0;
        this.totalElements = response.totalElements || 0;
        this.currentPage = response.number || 0;
        this.isLoading = false;
      },
      error: (error: any) => {
        console.error('Error loading blogs:', error);
        this.snackBar.open('Failed to load blogs', 'Close', { duration: 3000 });
        this.isLoading = false;
      }
    });
  }

  viewBlogDetails(blogId: string | undefined): void {
    if (blogId) {
      this.router.navigate(['/blog', blogId]);
    }
  }

  likePost(postId: string | undefined, event: Event): void {
    event.stopPropagation();
    if (postId) {
      this.blogService.likePost(postId).subscribe({
        next: (response: any) => {
          this.snackBar.open('Post liked!', 'Close', { duration: 2000 });
          this.loadBlogs(); // Reload to get updated like count
        },
        error: (error: any) => {
          console.error('Error liking post:', error);
          this.snackBar.open('Failed to like post', 'Close', { duration: 3000 });
        }
      });
    }
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadBlogs();
  }

  getTruncatedContent(content: string, maxLength: number = 150): string {
    if (content.length <= maxLength) {
      return content;
    }
    return content.substring(0, maxLength) + '...';
  }
}
