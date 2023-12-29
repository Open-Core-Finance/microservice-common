import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RateSourceComponent } from './rate-source.component';

describe('RateSourceComponent', () => {
  let component: RateSourceComponent;
  let fixture: ComponentFixture<RateSourceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RateSourceComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RateSourceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
