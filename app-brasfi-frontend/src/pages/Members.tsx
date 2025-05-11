import { mockMembers } from '../mocks/members'

export default function Members() {
  return (
    <div>
      <h1 className="text-3xl font-semibold mb-4">Members</h1>
      <ul className="space-y-4">
        {mockMembers.map(m => (
          <li key={m.id} className="border rounded p-4 shadow-sm">
            <p className="font-medium">{m.name} (@{m.username})</p>
            <p className="text-sm text-gray-500">{m.email}</p>
          </li>
        ))}
      </ul>
    </div>
  )
}
