import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MaterialModules } from '../../AngularMaterialModule'; 
import { CommonModule } from '@angular/common';
import { BlogService, BlogPost } from '../../services/blog.service';

@Component({
  selector: 'app-create-post',
  imports: [ MaterialModules, ReactiveFormsModule, CommonModule ],
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css'
})
export class CreatePostComponent implements OnInit {

  postForm!: FormGroup;
  tags: string[] = [];
  isSubmitting = false;

  constructor( 
    private fb: FormBuilder, 
    private router: Router, 
    private blogService: BlogService,
    private snackBar: MatSnackBar
  ) { }


  ngOnInit() {
    this.postForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      content: ['', [Validators.required, Validators.maxLength(5000)]],
      img: ['', Validators.required],
      postedBy: ['', Validators.required],
    });
  }


  add(event: any){
    const value = (event.value || '').trim();
    if(value){
      this.tags.push(value);
    }

    event.chipInput!.clear();
  }

  remove(tag: any){
    const index = this.tags.indexOf(tag);

    if(index >= 0){
      this.tags.splice(index, 1);
    }
  }

  createPost() {
    if (this.postForm.invalid) {
      this.snackBar.open('Please fill in all required fields', 'Close', { duration: 3000 });
      return;
    }

    this.isSubmitting = true;

    const blogPost: BlogPost = {
      name: this.postForm.value.name,
      content: this.postForm.value.content,
      image: this.postForm.value.img,
      postedBy: this.postForm.value.postedBy,
      blogTags: this.tags
    };

    this.blogService.createBlog(blogPost).subscribe({
      next: (response: any) => {
        this.snackBar.open('Blog post created successfully!', 'Close', { duration: 3000 });
        this.isSubmitting = false;
        this.router.navigate(['/posts']);
      },
      error: (error: any) => {
        console.error('Error creating blog post:', error);
        this.snackBar.open('Failed to create blog post', 'Close', { duration: 3000 });
        this.isSubmitting = false;
      }
    });
  }
}
