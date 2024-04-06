import { GeneralModel, ListableItem } from "../CommonClasses";
import { UserDto} from "../User";
import { AccountState } from "../accounts/AccountState";

export abstract class Product implements GeneralModel<string>, ListableItem {
    id: string = "";
    name: string = "";
    index: number = 0;
    category: string = "";
    type: string = "";
    description: string = "";
    activated: boolean = true;
    productAvailabilities: ProductAvailability[] = [
      new ProductAvailability(ProductAvailabilityMode.ALL_GROUPS, []),
      new ProductAvailability(ProductAvailabilityMode.ALL_BRANCHES, [])
    ];
    productAvailabilityAllGroups: boolean = true;
    productAvailabilityAllBranches: boolean = true;
    productAvailabilityModeInfo: string[] = [];

    newAccountSetting: ProductNewAccountSetting = new ProductNewAccountSetting();
    currencies: string[] = [];

    // Product Fees
    allowArbitraryFees: boolean  = false;
    showInactiveFees: boolean  = false;

    lastModifiedDate: Date = new Date();
    createdDate: Date = new Date();
    createdBy: UserDto = new UserDto();
    lastModifiedBy: UserDto = new UserDto();
}

export enum MonthlyPayOption {
    /**
     * Pay monthly base on activation date.
     */
    MONTHLY_FROM_ACTIVATION = "MONTHLY_FROM_ACTIVATION",
    /**
     * Pay first day every month.
     */
    MONTHLY_FIRST_DAY = "MONTHLY_FIRST_DAY",
    /**
     * Pay last day every month.
     */
    MONTHLY_LAST_DAY = "MONTHLY_LAST_DAY"
}

export class ProductAvailability {
    constructor(public availabilityMode: ProductAvailabilityMode, public modeInfo: string[]) {
    }
}

export enum ProductAvailabilityMode {
    ALL_BRANCHES = "ALL_BRANCHES",
    SPECIFIC_BRANCHES = "SPECIFIC_BRANCHES",
    ALL_GROUPS = "ALL_GROUPS",
    SPECIFIC_GROUPS = "SPECIFIC_GROUPS"
}

export class ProductNewAccountSetting {
    type: ProductNewAccountSettingType = ProductNewAccountSettingType.INCREASEMENT;
    randomPatternTemplate: string = "@@@$#####";
    increasementStartingFrom: number = 0;
    initialState: AccountState = AccountState.NEW;
    fixAccountLength: number | null = 6;
    fixLengthId = true;
    idPrefix = "";
    idSuffix = "";
}

export enum ProductNewAccountSettingType {
  /**
   * Sequential ID numbers that begin with a specified number.
   */
  INCREASEMENT = "INCREASEMENT",
  /**
   * Generated UUID.
   */
  UUID = 'UUID',
  /**
   * Templates consist of the characters `#`, `@`, and `$`, where `#` specifies a number, `@` a letter, and `$` a number or a letter, chosen at random.
   * For example, `@#@#$` will configure system to generate five-character values of one letter, one number, one letter, one number, and one character
   * that is either a letter or a number, such as `B8J4P` or `P1F62`.
   */
  RANDOM_PATTERN = "RANDOM_PATTERN"
}
