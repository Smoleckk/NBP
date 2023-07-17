import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WelcomeComponent } from './pages/welcome/welcome.component';
import { UsersActivityComponent } from './pages/users-activity/users-activity.component';

const routes: Routes = [
  {
    component:WelcomeComponent,
    path:''
  },
  {
    component:UsersActivityComponent,
    path:'user-activity'
  },
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
