import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReactiveFormTextareaComponent } from './reactive-form-textarea.component';

describe('ReactiveFormTextareaComponent', () => {
  let component: ReactiveFormTextareaComponent;
  let fixture: ComponentFixture<ReactiveFormTextareaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactiveFormTextareaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReactiveFormTextareaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
