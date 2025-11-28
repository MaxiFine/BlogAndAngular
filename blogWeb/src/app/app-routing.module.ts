import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreatePostComponent } from './pages/create-post/create-post.component';
import { BlogListComponent } from './pages/blog-list/blog-list.component';
import { BlogDetailComponent } from './pages/blog-detail/blog-detail.component';

const routes: Routes = [
  { path: '', redirectTo: '/posts', pathMatch: 'full' },
  { path: 'posts', component: BlogListComponent },
  { path: 'blog/:id', component: BlogDetailComponent },
  { path: 'create-post', component: CreatePostComponent },
  { path: '**', redirectTo: '/posts' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
