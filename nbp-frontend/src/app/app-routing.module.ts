import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UsersActivityComponent } from './pages/users-activity/users-activity.component';
import { CurrenciesComponent } from './pages/currencies/currencies.component';

const routes: Routes = [
  {
    component: CurrenciesComponent,
    path: '',
  },
  {
    component: UsersActivityComponent,
    path: 'user-activity',
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
