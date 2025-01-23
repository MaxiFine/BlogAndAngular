import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
// import { AngularMaterialModule } from './AngularMaterialModule';
import { MaterialModule } from './AngularMaterialModule';


import { Component } from '@angular/core';

import { RouterOutlet } from '@angular/router';

import { MatIconModule } from '@angular/material/icon';
@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule, 
    AppRoutingModule,
    BrowserAnimationsModule, 
    MaterialModule,
    FormsModule, 
    ReactiveFormsModule,
     HttpClient
  
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
