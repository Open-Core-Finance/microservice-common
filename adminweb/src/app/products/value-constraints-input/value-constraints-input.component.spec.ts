import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValueConstraintsInputComponent } from './value-constraints-input.component';

describe('ValueConstraintsInputComponent', () => {
  let component: ValueConstraintsInputComponent;
  let fixture: ComponentFixture<ValueConstraintsInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ValueConstraintsInputComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ValueConstraintsInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
