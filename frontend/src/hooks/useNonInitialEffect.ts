import { DependencyList, EffectCallback, useEffect, useRef } from "react";

// https://github.com/thivi/use-non-initial-effect-hook

/**
 * This hook gets called only when the dependencies change but not during initial render.
 *
 * @param {EffectCallback} effect The `useEffect` callback function.
 * @param {DependencyList} deps An array of dependencies.
 *
 * @example
 * ```
 *  useNonInitialEffect(()=>{
 *      alert("Dependency changed!");
 * },[dependency]);
 * ```
 */
export const useNonInitialEffect = (
    effect: EffectCallback,
    deps?: DependencyList
) => {
    const initialRender = useRef(true);

    useEffect(() => {
        let effectReturns: void | (() => void);

        if (initialRender.current) {
            initialRender.current = false;
        } else {
            effectReturns = effect();
        }

        if (effectReturns && typeof effectReturns === "function") {
            return effectReturns;
        }
    }, deps);
};
