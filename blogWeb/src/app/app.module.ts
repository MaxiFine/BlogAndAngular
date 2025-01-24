import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
// import { AngularMaterialModule } from './AngularMaterialModule';
import { MaterialModules } from './AngularMaterialModule';


import { Component } from '@angular/core';

import { RouterOutlet } from '@angular/router';

import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
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
  providers: [],
  bootstrap: []
})
export class AppModule {}
