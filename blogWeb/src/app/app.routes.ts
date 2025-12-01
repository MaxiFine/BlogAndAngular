import { Routes } from '@angular/router';
import { BlogListComponent } from './pages/blog-list/blog-list.component';
import { BlogDetailComponent } from './pages/blog-detail/blog-detail.component';
import { CreatePostComponent } from './pages/create-post/create-post.component';

export const routes: Routes = [
  { path: '', redirectTo: '/posts', pathMatch: 'full' },
  { path: 'posts', component: BlogListComponent },
  { path: 'blog/:id', component: BlogDetailComponent },
  { path: 'create-post', component: CreatePostComponent },
  { path: '**', redirectTo: '/posts' }
];
