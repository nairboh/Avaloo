package ca.brianho.avaloo.fragments.game

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ca.brianho.avaloo.R
import ca.brianho.avaloo.network.WSInterface
import ca.brianho.avaloo.utils.boardFragment
import ca.brianho.avaloo.utils.moshi
import ca.brianho.avaloo.utils.playerMap
import ca.brianho.avaloo.utils.wsListener
import com.squareup.moshi.Types
import kotlinx.android.synthetic.main.fragment_role.*
import org.json.JSONArray
import org.json.JSONObject


class RoleFragment : Fragment(), WSInterface {
    private var message = ""

    override fun handleResponseMessage(message: String?) {
        this.message = message!!
        boardFragment = BoardFragment()
        wsListener.setInterface(boardFragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_role, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val json = JSONObject(message)

        val mapType = Types.newParameterizedType(Map::class.java, String::class.java, String::class.java)
        val adapter = moshi.adapter<Map<String, String>>(mapType)
        val data = adapter.fromJson(JSONObject(json["playerList"].toString()).toString())
        playerMap = data!!

        val name = (JSONObject(json["role"].toString())["name"]).toString()
        val team = (JSONObject(json["role"].toString())).toString()
        val knowledge = JSONArray(JSONObject(json["role"].toString())["knowledge"].toString())

        var listTest = ""
        for (i in 0 until knowledge.length()) {
            listTest += knowledge[i].toString() + ", "
        }

        textView.text = "You are " + name + "("+ team + "), you know " + knowledge
    }
}
