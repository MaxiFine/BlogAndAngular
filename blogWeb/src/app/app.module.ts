import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppComponent } from './app.component';
import { MaterialModules } from './material-modules';
import { AppRoutingModule } from './app-routing.module';

@NgModule({
  imports: [BrowserModule, BrowserAnimationsModule, MaterialModules, AppComponent],
  providers: [],
  bootstrap: [],
})
export class AppModule {}
