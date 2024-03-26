import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { AbstractEntityListService } from './abstract.entity.list.service';
import { Region } from '../classes/geocode/Region';
import { SubRegion } from '../classes/geocode/SubRegion';
import { Country } from '../classes/geocode/Country';
import { State } from '../classes/geocode/State';

@Injectable({
  providedIn: 'root'
})
export class RegionService extends AbstractEntityListService<Region> {

    override get entityServiceUrl(): string {
        return environment.apiUrl.region + "/";
    }

}

@Injectable({
    providedIn: 'root'
})
export class SubRegionService extends AbstractEntityListService<SubRegion> {

    override get entityServiceUrl(): string {
        return environment.apiUrl.subRegion + "/";
    }

}

@Injectable({
    providedIn: 'root'
})
export class CountryService extends AbstractEntityListService<Country> {

    override get entityServiceUrl(): string {
        return environment.apiUrl.country + "/";
    }

}

@Injectable({
    providedIn: 'root'
})
export class StateService extends AbstractEntityListService<State> {

    override get entityServiceUrl(): string {
        return environment.apiUrl.state + "/";
    }

}