import { Component } from '@angular/core';
import { Form, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-create-post',
  imports: [],
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css'
})
export class CreatePostComponent {

  postForm!: FormGroup;
  tags: string[] = [];

  constructor( private fb: FormBuilder, 
    private router: Router, 
    // private postService: PostService,
    private snackBar: MatSnackBar,
    // private dialog: MatDialog
  ) { }


  ngOnInit() {
    this.postForm = this.fb.group({
      name: [null, Validators.required],
      content: [null, Validators.maxLength(5000)],
      img: [null, Validators.required],
      postedBy: [null, Validators.required],
    });
  }


  add(event:any){
    const value = (event.value || '').trim();
    if(value){
      this.tags.push(value);
    }

    event.chipInput!.clear();
  }

  remove(tag: string){
    // this.tags.splice(this.tags.indexOf(tag), 1);
    const index = this.tags.indexOf(tag);

    if(index >= 0){
      this.tags.splice(index, 1);
    }
  }
}
