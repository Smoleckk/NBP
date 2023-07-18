import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsersActivityComponent } from './users-activity.component';

describe('UsersActivityComponent', () => {
  let component: UsersActivityComponent;
  let fixture: ComponentFixture<UsersActivityComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UsersActivityComponent],
    });
    fixture = TestBed.createComponent(UsersActivityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
