import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddOrganizationComponent } from './add-organization.component';

describe('AddChoolComponent', () => {
  let component: AddChoolComponent;
  let fixture: ComponentFixture<AddChoolComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddChoolComponent]
    });
    fixture = TestBed.createComponent(AddChoolComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
