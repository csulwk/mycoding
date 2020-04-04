import { validUsername, isExternal } from '@/utils/validate.js'

describe('Utils:validate', () => {
  it('validUsername', () => {
    expect(validUsername('admin')).toBe(true)
    expect(validUsername('delver')).toBe(true)
    expect(validUsername('tester')).toBe(false)
  })
  it('isExternal', () => {
    expect(isExternal('/dashboard')).toBe(true)
    expect(isExternal('./dashboard')).toBe(false)
    expect(isExternal('dashboard')).toBe(false)
  })
})
