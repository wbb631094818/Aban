/*
 * Copyright (c) 2018. Arash Hatami
 */
package injection

import common.AbanApplication
import common.AbanDialog
import common.util.ContactImageLoader
import common.widget.AvatarView
import common.widget.PagerTitleView
import common.widget.PreferenceView
import common.widget.AbanEditText
import common.widget.AbanSwitch
import common.widget.AbanTextView
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import feature.compose.DetailedChipView
import feature.widget.WidgetAdapter
import feature.widget.WidgetProvider
import receiver.DefaultSmsChangedReceiver
import receiver.MarkReadReceiver
import receiver.MarkSeenReceiver
import receiver.MmsReceivedReceiver
import receiver.MmsSentReceiver
import receiver.MmsUpdatedReceiver
import receiver.NightModeReceiver
import receiver.RemoteMessagingReceiver
import receiver.SendSmsReceiver
import receiver.SmsDeliveredReceiver
import receiver.SmsProviderChangedReceiver
import receiver.SmsReceiver
import receiver.SmsSentReceiver
import service.HeadlessSmsSendService
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, BuildersModule::class])
interface AppComponent {

    fun inject(application: AbanApplication)

    fun inject(dialog: AbanDialog)

    fun inject(fetcher: ContactImageLoader.ContactImageFetcher)

    fun inject(receiver: DefaultSmsChangedReceiver)
    fun inject(receiver: SmsDeliveredReceiver)
    fun inject(receiver: SmsSentReceiver)
    fun inject(receiver: MarkSeenReceiver)
    fun inject(receiver: MarkReadReceiver)
    fun inject(receiver: MmsReceivedReceiver)
    fun inject(receiver: MmsSentReceiver)
    fun inject(receiver: MmsUpdatedReceiver)
    fun inject(receiver: NightModeReceiver)
    fun inject(receiver: RemoteMessagingReceiver)
    fun inject(receiver: SendSmsReceiver)
    fun inject(receiver: SmsProviderChangedReceiver)
    fun inject(receiver: SmsReceiver)
    fun inject(receiver: WidgetProvider)

    fun inject(service: HeadlessSmsSendService)
    fun inject(service: WidgetAdapter)

    fun inject(view: AvatarView)
    fun inject(view: DetailedChipView)
    fun inject(view: PagerTitleView)
    fun inject(view: PreferenceView)
    fun inject(view: AbanEditText)
    fun inject(view: AbanSwitch)
    fun inject(view: AbanTextView)

}