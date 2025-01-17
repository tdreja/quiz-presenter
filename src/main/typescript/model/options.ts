export class Options<TYPE> {

    private _options: Array<TYPE>;

    public constructor(options: Array<TYPE>) {
        this._options = options;
    }

    public reserveNextRandom(): TYPE | null {
        if(this._options.length === 0) {
            return null;
        }
        const randNr = Math.floor(Math.random() * this._options.length);
        const result = this._options[randNr];
        this._options = this._options.filter(opt => opt !== result);
        return result;
    }

    public reserveOption(option: TYPE) {
        if(this._options.includes(option)) {
            this._options = this._options.filter(opt => opt !== option);
            return true;
        }
        return false;
    }

    public freeOption(option: TYPE): boolean {
        if(this._options.includes(option)) {
            return false;
        }
        this._options.push(option);
        return true;
    }
}