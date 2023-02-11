import { HttpStatusCode } from "axios";

import { useGet } from "../requests/useGet";

export interface GetSubscriptionInput {
    subscriptionUrl: string;
}

export const useGetSubscription = () => {
    const { loading, callGet } = useGet();

    async function getSubscription({
        subscriptionUrl,
    }: GetSubscriptionInput): Promise<HttpStatusCode> {
        const response = await callGet(subscriptionUrl, {}, true);

        return response.status;
    }

    return { loading, getSubscription };
};
