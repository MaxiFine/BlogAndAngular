import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MaterialModules } from './AngularMaterialModule';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MaterialModules],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'blogWeb';
}
