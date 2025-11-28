import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MaterialModules } from './AngularMaterialModule';
import { CommonModule } from '@angular/common';

import { BlogService } from './services/blog.service';
import { CommentService } from './services/comment.service';

@NgModule({
  declarations: [
  
  ],
  imports: [
    BrowserModule, 
    AppRoutingModule,
    BrowserAnimationsModule, 
    MaterialModules,
    FormsModule, 
    ReactiveFormsModule,
    HttpClientModule,
    AppComponent,
    CommonModule,
  ],
  providers: [BlogService, CommentService],
  bootstrap: []
})
export class AppModule {}
