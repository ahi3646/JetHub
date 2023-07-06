package com.hasan.jetfasthub.data

import android.content.Context
import com.hasan.jetfasthub.networking.RetrofitInstance
import com.hasan.jetfasthub.screens.main.organisations.model.OrganisationMemberModel
import com.hasan.jetfasthub.utility.Constants
import retrofit2.Response

interface OrganisationRepository{

    suspend fun getOrganisationMembers(token: String, organisation: String, page: Int): Response<OrganisationMemberModel>

}


class OrganisationImpl(private val context: Context) : OrganisationRepository {

    override suspend fun getOrganisationMembers(token: String, organisation: String, page: Int): Response<OrganisationMemberModel> {
        return RetrofitInstance(context = context).gitHubService.getOrganisationMembers(
            authToken = "Bearer ${Constants.PERSONAL_ACCESS_TOKEN}",
            organisation = organisation,
            page = page
        )
    }

}