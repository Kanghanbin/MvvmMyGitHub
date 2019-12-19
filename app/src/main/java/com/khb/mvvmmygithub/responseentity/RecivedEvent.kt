package com.khb.mvvmmygithub.responseentity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.khb.mvvmmygithub.utils.fromJson
import com.khb.mvvmmygithub.utils.toJson

/**
 *创建时间：2019/11/28
 *编写人：kanghb
 *功能描述：
 */
@Entity(tableName = "received_event")
@TypeConverters(RecivedEventTypeConverter::class)
data class ReceivedEvent(
    val actor: Actor,
    val created_at: String,
    @PrimaryKey
    val id: String,
    val payload: Payload,
    @SerializedName("public")
    @ColumnInfo(name = "is_public")
    val isPublic: Boolean,
    val repo: Repo,
    val type: Type
) {
    var indexInResponse: Int = -1
}

data class Actor(
    val avatar_url: String,
    val display_login: String,
    val gravatar_id: String,
    val id: Int,
    val login: String,
    val url: String
)

data class Payload(
    val action: String
)

data class Repo(
    val id: Int,
    val name: String,
    val url: String
)

enum class Type {
    WatchEvent,
    ForkEvent,
    PushEvent,
    CreateEvent,
    MemberEvent,
    PublicEvent,
    IssuesEvent,
    IssueCommentEvent,
    CheckRunEvent,
    CheckSuiteEvent,
    CommitCommentEvent,
    DeleteEvent,
    DeploymentEvent,
    DeploymentStatusEvent,
    DownloadEvent,
    FollowEvent,
    ForkApplyEvent,
    GitHubAppAuthorizationEvent,
    GistEvent,
    GollumEvent,
    InstallationEvent,
    InstallationRepositoriesEvent,
    MarketplacePurchaseEvent,
    LabelEvent,
    MembershipEvent,
    MilestoneEvent,
    OrganizationEvent,
    OrgBlockEvent,
    PageBuildEvent,
    ProjectCardEvent,
    ProjectColumnEvent,
    ProjectEvent,
    PullRequestEvent,
    PullRequestReviewEvent,
    PullRequestReviewCommentEvent,
    ReleaseEvent,
    RepositoryEvent,
    RepositoryImportEvent,
    RepositoryVulnerabilityAlertEvent,
    SecurityAdvisoryEvent,
    StatusEvent,
    TeamEvent,
    TeamAddEvent
}

val SUPPORT_EVENT_TYPES: List<Type> = listOf(
    Type.WatchEvent,
    Type.ForkEvent,
    Type.PushEvent,
    Type.CreateEvent
)

class RecivedEventTypeConverter {
    @TypeConverter
    fun storePayload2String(data:Payload): String = data.toJson()
    @TypeConverter
    fun storeString2Payload(value: String):Payload = value.fromJson()

    @TypeConverter
    fun storeActor2String(data: Actor): String = data.toJson()

    @TypeConverter
    fun storeString2Actor(value: String): Actor = value.fromJson()

    @TypeConverter
    fun storeRepo2String(data: Repo): String = data.toJson()

    @TypeConverter
    fun storeString2Repo(value: String): Repo = value.fromJson()

    // Type
    @TypeConverter
    fun restoreEnum(enumName: String): Type = Type.valueOf(enumName)

    @TypeConverter
    fun saveEnum2String(enumType: Type) = enumType.name
}