import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { BlogService, BlogPost } from '../../services/blog.service';
import { CommentService, Comment, CommentResponse } from '../../services/comment.service';
import { MaterialModules } from '../../AngularMaterialModule';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-blog-detail',
  standalone: true,
  imports: [MaterialModules, CommonModule, ReactiveFormsModule],
  templateUrl: './blog-detail.component.html',
  styleUrl: './blog-detail.component.css'
})
export class BlogDetailComponent implements OnInit {
  blog: BlogPost | null = null;
  comments: CommentResponse[] = [];
  commentForm!: FormGroup;
  isLoading = false;
  blogId: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private blogService: BlogService,
    private commentService: CommentService,
    private fb: FormBuilder,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.blogId = this.route.snapshot.paramMap.get('id');
    
    this.commentForm = this.fb.group({
      commentText: ['', [Validators.required, Validators.minLength(3)]],
      postedBy: ['', Validators.required]
    });

    if (this.blogId) {
      this.loadBlogDetails();
    }
  }

  loadBlogDetails(): void {
    if (!this.blogId) return;
    
    this.isLoading = true;
    this.blogService.getBlogDetails(this.blogId).subscribe({
      next: (response: BlogPost) => {
        this.blog = response;
        this.isLoading = false;
      },
      error: (error: any) => {
        console.error('Error loading blog details:', error);
        this.snackBar.open('Failed to load blog details', 'Close', { duration: 3000 });
        this.isLoading = false;
        this.router.navigate(['/posts']);
      }
    });
  }

  likePost(): void {
    if (!this.blogId) return;
    
    this.blogService.likePost(this.blogId).subscribe({
      next: (response: any) => {
        this.snackBar.open('Post liked!', 'Close', { duration: 2000 });
        this.loadBlogDetails(); // Reload to get updated like count
      },
      error: (error: any) => {
        console.error('Error liking post:', error);
        this.snackBar.open('Failed to like post', 'Close', { duration: 3000 });
      }
    });
  }

  submitComment(): void {
    if (this.commentForm.invalid || !this.blogId) {
      this.snackBar.open('Please fill in all fields', 'Close', { duration: 3000 });
      return;
    }

    const comment: Comment = {
      commentText: this.commentForm.value.commentText,
      postedBy: this.commentForm.value.postedBy
    };

    this.commentService.createComment(this.blogId, comment).subscribe({
      next: (response: CommentResponse) => {
        this.snackBar.open('Comment added successfully!', 'Close', { duration: 2000 });
        this.commentForm.reset();
        // Add the new comment to the list
        this.comments.push(response);
      },
      error: (error: any) => {
        console.error('Error adding comment:', error);
        this.snackBar.open('Failed to add comment', 'Close', { duration: 3000 });
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/posts']);
  }
}
