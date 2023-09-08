<?php

namespace App\Form;

use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\RepeatedType; 
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\Email;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Component\Validator\Constraints\EqualTo;

class RegistrationType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('email', EmailType::class, [
                 'label' => 'Email',
                'attr' => ['class' => 'form-control', 'placeholder' => 'Enter your email...'],
                'constraints' => [
                    new NotBlank(),
                    new Email(),
                ],
            ])
            ->add('password', RepeatedType::class, [
               
                'type' => PasswordType::class,
                'invalid_message' => 'The password fields must match.',
                'required' => true,
                'first_options' => ['constraints' => [
                    new NotBlank(),
                    new Length(['min' => 8]),
                ], 'label' => 'Password',
                'attr' => ['class' => 'form-control', 'placeholder' => 'Enter your password...'],],
                'second_options' => [
                      'label' => 'Password Confirmation',
                'attr' => ['class' => 'form-control', 'placeholder' => 'Re-enter your password...'],
                    'constraints' => [
                    new NotBlank(),
                ]],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            // Configure your form options here if needed
        ]);
    }
}
