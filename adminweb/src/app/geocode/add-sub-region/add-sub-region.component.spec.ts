import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddSubRegionComponent } from './add-sub-region.component';

describe('AddSubRegionComponent', () => {
  let component: AddSubRegionComponent;
  let fixture: ComponentFixture<AddSubRegionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddSubRegionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddSubRegionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
