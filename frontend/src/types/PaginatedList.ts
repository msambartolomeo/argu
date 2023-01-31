// NOTE: This is a generic class that can be used for any type of data that is paginated.
// It parses the link header and stores the links to the first, last, previous and next pages.
export class PaginatedList<T> {
    data: T[];
    first: string;
    last: string;
    prev?: string;
    next?: string;

    constructor(data: T[], linkHeader: string) {
        this.data = data;

        const links = linkHeader.split(", ");
        const linkMap = new Map<string, string>();
        links.forEach((link) => {
            const [url, rel] = link.split("; ");
            const relValue = rel.split("=")[1].replace(/"/g, "");
            const urlWithoutBrackets = url.replace(/</g, "").replace(/>/g, "");
            linkMap.set(relValue, urlWithoutBrackets);
        });

        this.first = linkMap.get("first") as string;
        this.last = linkMap.get("last") as string;
        this.prev = linkMap.get("prev");
        this.next = linkMap.get("next");
    }
}
