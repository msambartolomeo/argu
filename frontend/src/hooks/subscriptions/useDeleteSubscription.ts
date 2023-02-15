import { HttpStatusCode } from "axios";

import { useDelete } from "../requests/useDelete";

export interface DeleteSubscriptionInput {
    subscriptionUrl: string;
}

export const useDeleteSubscription = () => {
    const { loading, callDelete } = useDelete();

    async function callDeleteSubscription({
        subscriptionUrl,
    }: DeleteSubscriptionInput): Promise<HttpStatusCode> {
        const response = await callDelete(subscriptionUrl, {}, true);
        return response.status;
    }

    return { loading, callDeleteSubscription };
};
