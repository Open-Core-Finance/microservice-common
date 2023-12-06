import { Injectable } from '@angular/core';
import { Order, OrderDirection, Paging, Sort } from "../classes/Paging";

@Injectable({ providedIn: 'root' })
export class CommonService {

    public normalizeStr(input: string) {
        return input.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
    }

    public containStr(input: string, source: string) {
        return this.normalizeStr(source).toLowerCase().includes(this.normalizeStr(input).toLowerCase());
    }

    public containAnyStr(input: string, sources: string[]) {
        for (const source in sources) {
            if (this.normalizeStr(source).toLowerCase().includes(this.normalizeStr(input).toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    buildPostStringBody(body: any): string {
        let requestBody = "";
        let index = 0;
        for (const prop of Object.keys(body)) {
            if (index === 0) {
                requestBody = prop + "=" + body[prop];
            } else {
                requestBody += "&" + prop + "=" + body[prop];
            }
            index++;
        }
        return requestBody;
    }

    shuffle(array: any[]) {
        let currentIndex = array.length;
        let randomIndex;

        // While there remain elements to shuffle...
        while (currentIndex != 0) {

            // Pick a remaining element...
            randomIndex = Math.floor(Math.random() * currentIndex);
            currentIndex--;

            // And swap it with the current element.
            [array[currentIndex], array[randomIndex]] = [
                array[randomIndex], array[currentIndex]];
        }
    }

    buildPaging(fullData: any[], itemPerPage: number, pageNumber: number, orders: Order[], customData: any) {
        const paging = new Paging();
        if (fullData == null) {
            return;
        }
        paging.totalElements = fullData.length;
        const mod = ~~(paging.totalElements / itemPerPage);
        const totalPage = (paging.totalElements / itemPerPage) > mod ? mod + 1 : mod;
        if (pageNumber >= totalPage) {
            pageNumber = totalPage - 1;
        }
        if (pageNumber < 0) {
            pageNumber = 0;
        }
        paging.pageNumber = pageNumber;
        paging.totalPages = totalPage;
        paging.pageSize = itemPerPage;
        paging.number = pageNumber;
        paging.first = pageNumber == 0;
        paging.last = pageNumber == totalPage - 1;
        paging.status = 0;
        paging.statusCode = "";
        paging.sort = new Sort();
        paging.sort.sorted = orders != null && orders.length > 0;
        paging.sort.unsorted = !paging.sort.sorted;
        paging.sort.orders = orders;
        // Sort and filter by page
        const tmpData = [];
        for (const data of fullData) {
            tmpData.push(data);
        }
        if (paging.sort.sorted) {
            tmpData.sort((a: any, b: any) => {
                for (const order of orders) {
                    const val1 = a[order.property];
                    const val2 = b[order.property];
                    if ((val1 != null && val2 == null) || val1 > val2) {
                        return OrderDirection.DESC.toUpperCase() == order.direction.toUpperCase() ? -1 : 1;
                    } else if ((val1 == null && val2 != null) || val1 < val2) {
                        return OrderDirection.DESC.toUpperCase() == order.direction.toUpperCase() ? 1 : -1;
                    }
                }
                return 0;
            });
        }
        const result = [];
        const offset = paging.pageNumber * paging.pageSize;
        const lastItem = (paging.pageNumber + 1) * paging.pageSize;
        for (let i = offset; i < tmpData.length && i < lastItem; i++) {
            const item = tmpData[i];
            result.push(item);
        }
        paging.result = result;
        paging.numberOfElements = result.length;
        return paging;
    }

    fromCharCode(code: number) {
        return String.fromCharCode(code);
    }

    isNullOrEmpty(value: string | null | undefined) {
        if (value == null || value == undefined) {
            return true;
        }
        value = value.trim();
        if (value === "") {
            return true;
        }
        return false;
    }
}
