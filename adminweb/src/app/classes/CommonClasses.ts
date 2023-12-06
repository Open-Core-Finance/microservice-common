export interface GeneralModel {
    get id(): string;
}

export interface ListableItem {
    set index(index: number);
    get index(): number;
}