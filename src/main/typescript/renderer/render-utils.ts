interface Changes<KEY> {
    readonly toDelete: Array<Element>;
    readonly toUpdate: Map<KEY, Element>;
    readonly toCreate: Array<KEY>;
    template: Element | null;
}

function checkElementForChanges<KEY, VALUE>(
    attribute: string,
    map: Map<KEY, VALUE>,
    item: Element,
    changes: Changes<KEY>
) {
    const attributeValue = item.getAttribute(attribute);
    if (!attributeValue) {
        changes.toDelete.push(item);
        return;
    }

    // Treat template special
    if (attributeValue === 'template') {
        changes.template = item;
        return;
    }

    // Only update the given KEY once!
    const key: KEY = attributeValue as KEY;
    if (map.has(key) && !changes.toUpdate.has(key)) {
        changes.toUpdate.set(key, item);
        return;
    }

    // Unused or duplicate instances are deleted!
    changes.toDelete.push(item);
}

function checkRequiredChanges<KEY,VALUE>(
    html: HTMLElement, 
    attribute: string,
    map: Map<KEY, VALUE>
): Changes<KEY> {
    // Find all changes
    const changes: Changes<KEY> = {
        toDelete: [],
        toCreate: [],
        toUpdate: new Map(),
        template: null,
    };
    html.querySelectorAll(`[${attribute}]`).forEach((item) =>
        checkElementForChanges(attribute, map, item, changes)
    );
    map.forEach((_, key) => {
        if(!changes.toUpdate.has(key)) {
            changes.toCreate.push(key);
        }
    });

    return changes;
}

function updateElement<KEY,VALUE>(key: KEY, element: Element, map: Map<KEY,VALUE>, updater: (html: Element, key: KEY, value: VALUE) => void) {
    const value = map.get(key);
    if(!value) {
        return;
    }
    updater(element, key, value);
}

export function updateFromMap<KEY, VALUE>(
    html: HTMLElement,
    attribute: string,
    map: Map<KEY, VALUE>,
    updater: (html: Element, key: KEY, value: VALUE) => void
) {
    // Find all changes
    const changes: Changes<KEY> = checkRequiredChanges(html, attribute, map);
    const template = changes.template;
    if(!template) {
        console.warn('No template found!', html);
        return;
    }

    // Delete unused first
    for(const item of changes.toDelete) {
        if(item.parentNode) {
            item.parentNode.removeChild(item);
        }
    }

    // Update existing next
    changes.toUpdate.forEach((item,key) => updateElement(key, item, map, updater));

    // Create the missing ones
    for(const key of changes.toCreate) {
        const element = template.cloneNode(true) as Element;
        element.setAttribute(attribute, `${key}`);
        html.appendChild(element);
        updateElement(key, element, map, updater);
    }
}
