export interface GeneralModel<T> {
    get id(): T;
}

export interface ListableItem {
    set index(index: number);
    get index(): number;
}