import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubRegionComponent } from './sub-region.component';

describe('SubRegionComponent', () => {
  let component: SubRegionComponent;
  let fixture: ComponentFixture<SubRegionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubRegionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SubRegionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
