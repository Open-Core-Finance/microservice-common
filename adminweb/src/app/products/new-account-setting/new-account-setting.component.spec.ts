import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewAccountSettingComponent } from './new-account-setting.component';

describe('NewAccountSettingComponent', () => {
  let component: NewAccountSettingComponent;
  let fixture: ComponentFixture<NewAccountSettingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewAccountSettingComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NewAccountSettingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
