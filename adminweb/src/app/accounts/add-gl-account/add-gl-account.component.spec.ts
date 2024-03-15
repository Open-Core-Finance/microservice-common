import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddGlAccountComponent } from './add-gl-account.component';

describe('AddGlAccountComponent', () => {
  let component: AddGlAccountComponent;
  let fixture: ComponentFixture<AddGlAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddGlAccountComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddGlAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
