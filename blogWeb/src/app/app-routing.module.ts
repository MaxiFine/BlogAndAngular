import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreatePostComponent } from './pages/create-post/create-post.component';
// import { HomeComponent } from './AngularMaterialModule'; // Replace with your actual components
// import { AboutComponent } from './app.component.spec';

const routes: Routes = [
  { path: 'create-post', component:  CreatePostComponent }, // Default route
//   { path: 'about', component:  }, // Example route
//   { path: '**', redirectTo: '' }, // Wildcard route for 404
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
